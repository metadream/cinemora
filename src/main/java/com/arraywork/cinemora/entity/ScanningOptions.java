package com.arraywork.cinemora.entity;

import lombok.Data;

/**
 * 扫描选项
 *
 * @author Marco
 * @copyright ArrayWork Inc.
 * @since 2025/04/20
 */
@Data
public class ScanningOptions {

    private boolean forceRebuild;
    private boolean cleanIndexes;

}