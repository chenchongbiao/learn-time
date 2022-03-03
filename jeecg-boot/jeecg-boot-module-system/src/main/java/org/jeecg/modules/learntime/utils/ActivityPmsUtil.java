package org.jeecg.modules.learntime.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.learntime.entity.ActivityRecord;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

@Slf4j
@Component
public class ActivityPmsUtil {
    private static String uploadPath;

    @Value("${jeecg.path.upload}")
    public void setUploadPath(String uploadPath) {
        ActivityPmsUtil.uploadPath = uploadPath;
    }

    public static String saveExcelByList(List<ActivityRecord> errorList, String name) {
        Date d = new Date();
        String saveDir = "logs" + File.separator + DateUtils.yyyyMMdd.get().format(d) + File.separator;
        String saveFullDir = ActivityPmsUtil.uploadPath + File.separator + saveDir;

        File saveFile = new File(saveFullDir);
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
        name += DateUtils.yyyymmddhhmmss.get().format(d) + Math.round(Math.random() * 10000);
        String saveFilePath = saveFullDir + name + ".xls";

        try {
            List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", new ExportParams(name,name, ExcelType.XSSF));//表格Title
            map.put("entity",ActivityRecord.class);//表格对应实体
            map.put("data", errorList);
            listMap.add(map);
            Workbook workbook = ExcelExportUtil.exportExcel(listMap, ExcelType.XSSF);
            FileOutputStream fos = new FileOutputStream(saveFilePath);
            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
//            log.info("excel导入生成错误日志文件异常:" + e.getMessage());
        }
        return saveDir + name + ".xls";
    }
}
