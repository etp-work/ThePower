using System;
using System.Collections.Generic;
using System.Windows.Forms;
using System.ComponentModel;
using System.Diagnostics;
using System.Net;
using System.Net.Sockets;
using SuperSocket.SocketBase;
using SuperWebSocket;
using System.Configuration;
using System.Reflection;
using log4net;
using Newtonsoft.Json.Linq;

[assembly: log4net.Config.XmlConfigurator(Watch = true)]
namespace GriffinsPortalKit
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>

        static NativeContainer nativeContainer;
        //static ThePower thePower;
        static StartupProgressBar progressBar;
        static WebSocketServer websocketserver;
        static Dictionary<String, WebSocketSession> sessions = new Dictionary<String, WebSocketSession>();
        public static Guid nativeID;
        public static Guid portalID;
        private static EmbeddedTomcatController tomcatController;
        static ILog logger = log4net.LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        [STAThread]
        static void Main()
        {
            try
            {
                tomcatController = new EmbeddedTomcatController();
                nativeID = Guid.NewGuid();
                portalID = Guid.NewGuid();
                websocketserver = new WebSocketServer();
                int port = Int32.Parse(ConfigurationManager.AppSettings["WebSocketPort"]);
                if (!websocketserver.Setup(port))
                {
                    throw new Exception("ERROR:" + port + " already used!");
                }

                for (int i = 0; i < 3; i++ )
                {
                    if (!websocketserver.Start())
                    {
                        EmbeddedTomcatController.killExist();
                        continue;
                    }
                    else {
                        break;
                    }
                    throw new Exception("ERROR:Failed to start server!");
                }


                websocketserver.NewMessageReceived += websocketserver_NewMessageReceived;
            }
            catch (Exception e)
            {
                logger.Error(e.Message, e);
                MessageBox.Show(e.Message);
                return;
            }

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            tomcatController.startTomcat();

            nativeContainer = new NativeContainer();
            nativeContainer.FormClosing += nativeContainer_FormClosing;
            nativeContainer.initPortalKitUrl();
            //thePower = new ThePower();
            progressBar = new StartupProgressBar();
            nativeContainer.ProgressBar = progressBar;
            progressBar.start();
            Application.Run(progressBar);
        }


        static void websocketserver_NewMessageReceived(WebSocketSession session, string value)
        {
            logger.Debug("WebSocket Server NewMessageReceived:" + value + ", Session:" + session.SessionID);
            if (value.StartsWith("ID:"))
            {
                sessions.Remove(value);
                sessions.Add(value, session);
            }
            else
            {
                try
                {
                    JObject json = JObject.Parse(value);
                    string to = "ID:" + (string)json["to"];
                    if (sessions.ContainsKey(to))
                    {
                        WebSocketSession toSession = sessions[to];
                        toSession.Send(value);
                    }
                    else
                    {
                        logger.Error("Message Destination Not Found:" + to);
                    }
                }
                catch (Exception ex)
                {
                    logger.Error(ex.Message, ex);
                }
            }
        }

        static void nativeContainer_FormClosing(object sender, FormClosingEventArgs e)
        {
            tomcatController.stopTomcat();

            if (null != websocketserver)
            {
                if (websocketserver.State == ServerState.Running)
                {
                    websocketserver.Stop();
                }
                websocketserver.Dispose();
                websocketserver = null;
            }
            //thePower.Close();
            progressBar.Close();
        }
    }
}
