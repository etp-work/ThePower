package org.etp.portalKit.powerbuild.service;

import java.io.File;
import java.text.MessageFormat;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.etp.portalKit.common.util.CommandResult;
import org.etp.portalKit.common.util.OutputHandler;
import org.etp.portalKit.common.util.ShellRunner;
import org.springframework.stereotype.Component;

/**
 * The purpose of this class is to provide a mvn build command runner.
 */
@Component(value = "mavenExecutor")
public class MavenExecutor {
    @Resource(name = "shellRunner")
    private ShellRunner runner;

    private String path_format = "{0}\\pom.xml";

    private String COMPILE_FORMAT = "mvn clean install -f \"{0}\" -Dmaven.test.skip=true";

    private String TEST_FORMAT = "mvn clean test -f \"{0}\"";

    private String COMPILE_TEST_FORMAT = "mvn clean install -f \"{0}\"";

    /**
     * Execute the specified command type to the project which
     * indicated by <code>path</code>.
     * 
     * @param path the project defined by this path will be executed.
     * @param type executed command type.
     *            <code>ExecuteType.COMPILE</code>,
     *            <code>ExecuteType.TEST</code>,
     *            <code>ExecuteType.COMPILE_TEST</code>
     * @return execution information
     */
    public CommandResult exec(String path, ExecuteType type) {
        String pom = convertPath(path);
        if (StringUtils.isBlank(pom)) {
            throw new RuntimeException("Given path is not a regular maven project path");
        }
        final StringBuffer sb = new StringBuffer();
        final CommandResult cr = new CommandResult();
        String command = null;
        switch (type) {
        case COMPILE:
            command = MessageFormat.format(COMPILE_FORMAT, pom);
            break;
        case TEST:
            command = MessageFormat.format(TEST_FORMAT, pom);
            break;
        case COMPILE_TEST:
            command = MessageFormat.format(COMPILE_TEST_FORMAT, pom);
            break;
        default:
            throw new RuntimeException("Wrong execute type.");
        }
        runner.setCmdline(command);
        runner.setOutputHandler(new OutputHandler() {
            @Override
            public void onOutRead(String message, boolean isErrorOut) {
                if (StringUtils.contains(message, "BUILD SUCCESS")) {
                    cr.setSuccess(true);
                } else if (StringUtils.contains(message, "BUILD FAILURE")) {
                    cr.setSuccess(false);
                }
                sb.append(message);
                sb.append(System.lineSeparator());
            }
        });
        cr.setStateCode(runner.run());
        if (cr.isSuccess()) {
            cr.setMessage("");
        } else {
            cr.setMessage(sb.toString());
        }
        return cr;
    }

    /**
     * convert the pom.xml format to a real absolute path of pom.xml
     * 
     * @param path a folder path which should contains a pom.xml
     * @return converted result
     */
    private String convertPath(String path) {
        String absPath = MessageFormat.format(path_format, path);
        File file = new File(absPath);
        if (!file.isFile()) {
            return null;
        }
        return absPath;
    }
}
