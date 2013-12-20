using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Microsoft.Win32;

namespace ThePowerBootstrap
{
    class SystemChecker
    {
        private static string[] DEFAULT_LOCATIONS = new string[] { @"C:\Program Files\The Power\DevelopmentToolkit.exe", @"C:\Program Files (x86)\The Power\DevelopmentToolkit.exe" };
        private static string REGISTRY_PATH = @"SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\ThePowerFromEricssonThreePerson_is1";

        public static string getThePowerLocation()
        {
            foreach (string _location in DEFAULT_LOCATIONS)
            {
                if (File.Exists(_location))
                {
                    return _location;
                }
            }

            RegistryKey registryKey = Registry.LocalMachine.OpenSubKey(REGISTRY_PATH);
            if (registryKey != null)
            {
                string _installpath = (string)registryKey.GetValue("InstallLocation");
                if (File.Exists(_installpath))
                {
                    return _installpath;
                }
            }

            return null;
        }

        public static bool is64bit()
        {
            bool is64bit = !string.IsNullOrEmpty(Environment.GetEnvironmentVariable("PROCESSOR_ARCHITEW6432"));
            return is64bit;
        }
    }
}