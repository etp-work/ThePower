package org.etp.portalKit.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * The purpose of this class is to provide a set of useful tools for
 * compressing stuffs.
 */
public class CompressUtil {

    private static int BUFFER_SIZE = 1024;

    /**
     * UnGzip a Gzip file, into a specified folder. Note: the original
     * gzip file won't be deleted.
     * 
     * @param filePath
     * @param destPath
     * @throws IOException
     */
    public static void unGzip(String filePath, String destPath) throws IOException {
        if (StringUtils.isBlank(filePath))
            throw new NullPointerException("The file you want to ungzip should not be null or empty.");
        File file = new File(filePath);
        if (!file.isFile())
            throw new FileNotFoundException("The file you want to ungzip should be a real file.");
        if (StringUtils.isBlank(destPath))
            throw new NullPointerException("The destination you want to ungzip should not be null or empty.");
        File dest = new File(destPath);
        if (!dest.isDirectory())
            throw new FileNotFoundException("The destination you want to ungzip should be a exist directory.");
        File destFile = new File(dest, new File(GzipUtils.getUncompressedFilename(filePath)).getName());
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE));
            os = new BufferedOutputStream(new FileOutputStream(destFile), BUFFER_SIZE);
            IOUtils.copy(is, os);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }

    }

    /**
     * UnGzip a Gzip file, into the same folder as original gzip file.
     * Note: the original gzip file won't be deleted.
     * 
     * @param filePath absolute path of gzip file.
     * @throws IOException
     */
    public static void unGzip(String filePath) throws IOException {
        if (StringUtils.isBlank(filePath))
            throw new NullPointerException("The file you want to ungzip should not be null or empty.");
        File file = new File(filePath);
        if (!file.isFile())
            throw new FileNotFoundException("The file you want to ungzip should be a real file.");
        unGzip(filePath, file.getParent());
    }

    /**
     * Untar a tar file, into the same folder as original tar file.
     * Note: the original tar file won't be deleted.
     * 
     * @param filePath
     * @throws IOException
     */
    public static void unTar(String filePath) throws IOException {
        if (StringUtils.isBlank(filePath))
            throw new NullPointerException("The file you want to untar should not be null or empty.");
        File file = new File(filePath);
        if (!file.isFile())
            throw new FileNotFoundException("The file you want to untar should be a real file.");
        unTar(filePath, file.getParent());
    }

    /**
     * Untar a tar file, into the specified folder. Note: the original
     * tar file won't be deleted.
     * 
     * @param filePath
     * @param destDir
     * @throws IOException
     */
    public static void unTar(String filePath, String destDir) throws IOException {
        if (StringUtils.isBlank(filePath))
            throw new NullPointerException("The file you want to untar should not be null or empty.");
        File file = new File(filePath);
        if (!file.isFile())
            throw new FileNotFoundException("The file you want to untar should be a real file.");
        TarArchiveInputStream is = null;
        try {
            is = new TarArchiveInputStream(new BufferedInputStream(new FileInputStream(filePath), BUFFER_SIZE));
            TarArchiveEntry entry = null;
            while ((entry = is.getNextTarEntry()) != null) {
                if (entry.isDirectory()) {
                    File directory = new File(destDir, entry.getName());
                    directory.mkdirs();
                } else {
                    BufferedOutputStream bos = null;
                    try {
                        byte[] buffer = new byte[BUFFER_SIZE];
                        File entryFile = new File(destDir, entry.getName());
                        if (!entryFile.getParentFile().exists())
                            entryFile.getParentFile().mkdirs();
                        bos = new BufferedOutputStream(new FileOutputStream(entryFile), BUFFER_SIZE);
                        int len;
                        long size = entry.getSize();
                        while (size > 0) {
                            if (size < BUFFER_SIZE) {
                                len = is.read(buffer, 0, (int) size);
                                size -= len;
                            } else {
                                len = is.read(buffer);
                                size -= len;
                            }
                            bos.write(buffer, 0, len);
                        }
                    } finally {
                        IOUtils.closeQuietly(bos);
                    }
                }
            }
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            long start = System.currentTimeMillis();
            unGzip("C:\\Users\\ehaozuo\\Downloads\\testing\\portal-root-cxp-R4B02-bin.tar.gz","C:\\Users\\ehaozuo\\Downloads\\");
            long end = System.currentTimeMillis();
            System.out.println((end - start));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
