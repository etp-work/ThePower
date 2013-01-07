package org.etp.portalKit.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.etp.portalKit.client.logic.AutoHideComposite;
import org.etp.portalKit.client.logic.CompositeInjector;

/**
 * The purpose of this class is
 */
public class PortalKitGui {
    private AutoHideComposite auto;
    private Browser browser;

    private String ip;
    private int port;
    private String url;

    private int WIDTH = 273;
    private int HEIGHT = 528;

    private String IP_PATTERN = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";

    /**
     * Creates a new instance of <code>PortalKitGui</code>.
     */
    public PortalKitGui() {
        auto = new AutoHideComposite();
        auto.setPanel(new CompositeInjector() {
            @Override
            public Composite ceatePanel(Composite composite) {
                browser = null;
                composite.setLayout(new FillLayout(SWT.HORIZONTAL));
                try {
                    browser = new Browser(composite, SWT.NONE);
                    browser.setLayoutData(new GridData(GridData.FILL_BOTH));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return browser;
            }
        });

        auto.setSize(WIDTH, HEIGHT);
        auto.setTitle("The Power");
    }

    /**
     * 
     */
    public void display() {
        auto.display();
    }

    /**
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     */
    public void setUrl() {
        if (browser == null)
            return;
        if (ip == null || ip.trim().equals("") || !ip.matches(IP_PATTERN))
            ip = "localhost";
        if (port == 0)
            port = 8080;
        if (url == null || url.trim().equals(""))
            url = "/PortalKit/";
        browser.setUrl("http://" + ip + ":" + port + url);
    }

    /**
     * @param url
     */
    public void setFullUrl(String url) {
        browser.setUrl(url);
    }

    private static String CONFIG = "setting.cfg";

    /**
     * @param args
     */
    public static void main(String[] args) {
        PortalKitGui gui = new PortalKitGui();
        File file = new File(CONFIG);
        Properties p = new Properties();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        String fullUrl = null;
        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis, Charset.forName("utf-8"));
            p.load(isr);
            if (p.containsKey("ip"))
                gui.setIp(p.getProperty("ip"));
            if (p.containsKey("port"))
                gui.setPort(Integer.parseInt(p.getProperty("port")));
            if (p.containsKey("url"))
                gui.setUrl(p.getProperty("url"));
            if (p.containsKey("fullurl"))
                fullUrl = p.getProperty("fullurl");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fullUrl != null && !fullUrl.trim().equals(""))
            gui.setFullUrl(fullUrl);
        else
            gui.setUrl();
        gui.display();
    }
}
