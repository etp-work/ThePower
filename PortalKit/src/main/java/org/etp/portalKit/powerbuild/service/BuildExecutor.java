package org.etp.portalKit.powerbuild.service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.etp.portalKit.common.shell.CommandResult;
import org.etp.portalKit.common.shell.OutputHandler;
import org.etp.portalKit.common.shell.ShellRunner;
import org.springframework.stereotype.Component;

/**
 * The purpose of this class is to provide a mvn build command runner.
 */
@Component(value = "buildExecutor")
public class BuildExecutor {
    @Resource(name = "shellRunner")
    private ShellRunner runner;

    private String path_format = "{0}\\pom.xml";

    private String cmd_format = "mvn clean install -f \"{0}\" -Dmaven.test.skip=true";

    private FileFilter filter;

    /**
     * Creates a new instance of <code>BuildExecutor</code>.
     */
    public BuildExecutor() {
        filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && StringUtils.endsWith(pathname.getName(), "war");
            }
        };
    }

    /**
     * Compile the specified path within maven command line. null will
     * be returned if path is not a directory or contains no pom.xml.
     * 
     * @param path the path which will be compiled.
     * @return execution information
     */
    public CommandResult compile(String path) {
        String pom = convertPath(path);
        if (StringUtils.isBlank(pom))
            throw new RuntimeException("Given path is not a regular maven project path");
        final StringBuffer sb = new StringBuffer();
        final CommandResult cr = new CommandResult();
        String command = MessageFormat.format(cmd_format, pom);
        runner.setCmdline(command);
        runner.setOutputHandler(new OutputHandler() {
            @Override
            public void onOutRead(String message, boolean isErrorOut) {
                if (StringUtils.contains(message, "BUILD SUCCESS"))
                    cr.setSuccess(true);
                else if (StringUtils.contains(message, "BUILD FAILURE"))
                    cr.setSuccess(false);
                sb.append(message);
                sb.append(System.lineSeparator());
            }
        });
        cr.setStateCode(runner.run());
        if (cr.isSuccess())
            cr.setMessage("");
        else
            cr.setMessage(sb.toString());
        return cr;
    }

    /**
     * Deploy the specified src to destination.
     * 
     * @param src a folder path indicate which project will be
     *            deployed.
     * @param dest a folder path indicate what place the war file
     *            should be moved into.
     * @return deploy
     */
    public boolean deploy(String src, String dest) {
        File warFile = getWarFile(src);
        if (warFile == null)
            throw new RuntimeException("Given src contains no target war file.");
        File destDir = new File(dest);
        try {
            FileUtils.copyFileToDirectory(warFile, destDir);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Deploy the specified set of packages to destination.
     * 
     * @param srcs a set of folder paths indicate which project will
     *            be deployed.
     * @param dest a folder path indicate what place the war file
     *            should be moved into.
     * @return deploy
     */
    public boolean deploy(List<String> srcs, String dest) {
        boolean isAllTrue = true;
        for (String src : srcs) {
            if (!deploy(src, dest))
                isAllTrue = false;
        }
        return isAllTrue;
    }

    /**
     * Given a compile folder, automatically scan the child target
     * folder, find the .war file.
     * 
     * @param src compile folder, should contains a pom.xml and a
     *            target child folder.
     * @return the compiled .war file.
     */
    private File getWarFile(String src) {
        File parent = new File(src);
        if (!parent.isDirectory())
            return null;
        File target = new File(parent, "target\\");
        File[] warfiles = target.listFiles(filter);
        if (warfiles.length > 0)
            return warfiles[0];
        return null;
    }

    /**
     * convert the pom.xml format to a real absolute path of pom.xml
     * @param path a folder path which should contains a pom.xml
     * @return converted result
     */
    private String convertPath(String path) {
        String absPath = MessageFormat.format(path_format, path);
        File file = new File(absPath);
        if (!file.isFile())
            return null;
        return absPath;
    }
}
