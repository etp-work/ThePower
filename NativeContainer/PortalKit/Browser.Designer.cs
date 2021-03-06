﻿namespace DevelopmentToolkit
{
    partial class Browser
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.splitContainer1 = new System.Windows.Forms.SplitContainer();
            this.geckoWebBrowser1 = new Gecko.GeckoWebBrowser();
            this.geckoWebBrowser2 = new Gecko.GeckoWebBrowser();
            this.splitContainer1.Panel1.SuspendLayout();
            this.splitContainer1.Panel2.SuspendLayout();
            this.splitContainer1.SuspendLayout();
            this.SuspendLayout();
            // 
            // splitContainer1
            // 
            this.splitContainer1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.splitContainer1.Location = new System.Drawing.Point(0, 0);
            this.splitContainer1.Name = "splitContainer1";
            // 
            // splitContainer1.Panel1
            // 
            this.splitContainer1.Panel1.Controls.Add(this.geckoWebBrowser1);
            // 
            // splitContainer1.Panel2
            // 
            this.splitContainer1.Panel2.Controls.Add(this.geckoWebBrowser2);
            this.splitContainer1.Size = new System.Drawing.Size(1014, 558);
            this.splitContainer1.SplitterDistance = 292;
            this.splitContainer1.TabIndex = 0;
            // 
            // geckoWebBrowser1
            // 
            this.geckoWebBrowser1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.geckoWebBrowser1.Location = new System.Drawing.Point(0, 0);
            this.geckoWebBrowser1.Name = "geckoWebBrowser1";
            this.geckoWebBrowser1.Size = new System.Drawing.Size(292, 558);
            this.geckoWebBrowser1.TabIndex = 0;
            this.geckoWebBrowser1.UseHttpActivityObserver = false;
            // 
            // geckoWebBrowser2
            // 
            this.geckoWebBrowser2.Dock = System.Windows.Forms.DockStyle.Fill;
            this.geckoWebBrowser2.Location = new System.Drawing.Point(0, 0);
            this.geckoWebBrowser2.Name = "geckoWebBrowser2";
            this.geckoWebBrowser2.Size = new System.Drawing.Size(718, 558);
            this.geckoWebBrowser2.TabIndex = 0;
            this.geckoWebBrowser2.UseHttpActivityObserver = false;
            // 
            // Browser
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1014, 558);
            this.Controls.Add(this.splitContainer1);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedToolWindow;
            this.Name = "Browser";
            this.StartPosition = System.Windows.Forms.FormStartPosition.Manual;
            this.Text = "Ericsson TV";
            this.splitContainer1.Panel1.ResumeLayout(false);
            this.splitContainer1.Panel2.ResumeLayout(false);
            this.splitContainer1.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.SplitContainer splitContainer1;
        private Gecko.GeckoWebBrowser geckoWebBrowser1;
        private Gecko.GeckoWebBrowser geckoWebBrowser2;

    }
}