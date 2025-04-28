package com.arraywork.cinemora;

import org.springframework.stereotype.Component;

import com.arraywork.autumn.util.NumberUtils;
import com.arraywork.autumn.util.TimeUtils;

/**
 * 全局模板工具
 *
 * @author Marco
 * @copyright arraywork.com
 * @since 2025/04/08
 */
@Component("utils")
public class CommonUtils {

    public String formatSiBytes(long bytes) {
        return NumberUtils.formatSiBytes(bytes);
    }

    public String formatDuration(long millis) {
        return TimeUtils.formatDuration(millis);
    }

}