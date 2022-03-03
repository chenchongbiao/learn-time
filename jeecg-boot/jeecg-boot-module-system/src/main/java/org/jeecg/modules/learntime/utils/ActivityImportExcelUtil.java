package org.jeecg.modules.learntime.utils;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.learntime.entity.ActivityRecord;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ActivityImportExcelUtil {

    public static Result<?> imporReturnXls(int errorLines, int successLines, List<ActivityRecord> errorList) throws IOException {
        if (errorLines == 0) {
            return Result.ok("共" + successLines + "行数据全部导入成功！");
        } else {
            JSONObject result = new JSONObject(5);
            int totalCount = successLines + errorLines;
            result.put("totalCount", totalCount);
            result.put("errorCount", errorLines);
            result.put("successCount", successLines);
            result.put("msg", "总上传行数：" + totalCount + "，已导入行数：" + successLines + "，错误行数：" + errorLines);
            String fileUrl = ActivityPmsUtil.saveExcelByList(errorList, "ImportExcelError");
            int lastIndex = fileUrl.lastIndexOf(File.separator);
            String fileName = fileUrl.substring(lastIndex + 1);
            result.put("fileUrl", "/sys/common/static/" + fileUrl);
            result.put("fileName", fileName);
            Result res = Result.ok(result);
            res.setCode(201);
            res.setMessage("文件导入成功，但有错误。");
            return res;
        }
    }
}
