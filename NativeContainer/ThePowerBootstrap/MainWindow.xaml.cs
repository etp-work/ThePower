using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.IO;
using System.Net;
using System.IO.Compression;
using System.Diagnostics;

namespace ThePowerBootstrap
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public static string THE_POWER_X86 = "ThePower-x86-1.0.5.exe.zip";
        public static string THE_POWER_X64 = "ThePower-x64-1.0.5.exe.zip";
        public static string SERVER_PACKAGE_PATH = "http://142.133.49.165/thepower/packages";
        public static string DOWNLOAD_DESC = "Downloading The Power: {0}%";
        public static string UNZIP_DESC = "Extracting installation files ...";
        public static string INSTALL_DESC = "Installing The Power ...";

        private string packageName = null;
        private string fullPackageUrl = null;
        private string localPackageFile = null;
        private string setupPath = null;
        private string setupFileName = null;


        public MainWindow()
        {
            InitializeComponent();
        }

        private void Window_Loaded(object sender, RoutedEventArgs e)
        {
            string thePowerPath = SystemChecker.getThePowerLocation();
            if (thePowerPath != null)
            {
                update();
            }
            else
            {
                install();
            }
        }

        private void update()
        {
            // TODO: Check for update
            start();
        }

        private void install()
        {
            packageName = SystemChecker.is64bit() ? THE_POWER_X64 : THE_POWER_X64;
            fullPackageUrl = String.Format("{0}/{1}", SERVER_PACKAGE_PATH, packageName);
            localPackageFile = @"./" + packageName;
            download(fullPackageUrl, localPackageFile);
        }

        private void download(string fullPackageUrl, string localFile)
        {
            WebClient webClient = new WebClient();
            this.description.Content = String.Format(DOWNLOAD_DESC, 0);
            webClient.DownloadFileCompleted += webClient_DownloadFileCompleted;
            webClient.DownloadProgressChanged += webClient_DownloadProgressChanged;
            webClient.DownloadFileAsync(new Uri(fullPackageUrl), localFile);
        }

        void webClient_DownloadProgressChanged(object sender, DownloadProgressChangedEventArgs e)
        {
            this.description.Content = String.Format(DOWNLOAD_DESC, e.ProgressPercentage);
            if (this.progressBar.IsIndeterminate)
            {
                this.progressBar.IsIndeterminate = false;
            }
            this.progressBar.Value = e.ProgressPercentage;
        }

        void webClient_DownloadFileCompleted(object sender, System.ComponentModel.AsyncCompletedEventArgs e)
        {
            this.description.Content = String.Format(DOWNLOAD_DESC, 100);
            this.progressBar.IsIndeterminate = true;
            unzip(localPackageFile);
        }

        void unzip(string fullFilePath)
        {
            this.description.Content = UNZIP_DESC;
            setupPath = @"./setup";
            if (Directory.Exists(setupPath))
            {
                Directory.Delete(setupPath, true);
            }
            ZipFile.ExtractToDirectory(fullFilePath, setupPath);
            setup();
        }

        void setup()
        {
            setupFileName = String.Format("{0}/{1}", setupPath, packageName.Substring(0, packageName.LastIndexOf(".zip")));
            if(File.Exists(setupFileName)){
                this.description.Content = INSTALL_DESC;
                ProcessStartInfo startInfo = new ProcessStartInfo();
                startInfo.CreateNoWindow = false;
                startInfo.UseShellExecute = false;
                startInfo.FileName = setupFileName;
                startInfo.WindowStyle = ProcessWindowStyle.Normal;
                Process.Start(startInfo);
                this.Close();
            }
        }

        void start()
        {

        }
    }
}
