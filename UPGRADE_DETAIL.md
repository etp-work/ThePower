Upgrade Information
=====================

Version 1.0.5 at 2013-10-12

  1. Upgrade view provider GeckoFX to 22.0 and the corresponding XULRunner.
  2. Add icon to use for Setup file.
  3. Add icon to display for the Uninstall entry in the Add/Remove Programs Control Panel applet.
  4. Reduce the time when compiling the source code on category level.
  5. Add indicator to category compile feature. End-user knows how many war files remain to be compiled.
  6. Remove dotted outline from DOM element.
  7. Remove one-key widgettool from ThePower, since we recommend to use standard widgettool.


Version 1.0.4 at 2013-08-12

  1. Add chosen list to Category view, because end-user may want to know what war files are included in the selected Category.
  2. Rename "common" to "Individual".
  3. Rename "category" to "Category".
  4. Adjust the height of Category view, since it is not aligned with Individual view.


Version 1.0.3 at 2013-xx-xx

  1. Update long polling mechanism from previous capbility that only support tomcat status monitor to support server side push.

  2. Update the maven execution log mechansim to be more accurate and friendly.


Version 1.0.2P at 2013-06-03

  1. Fix bug that one-key widget doesn't work for snapshot MSDK.



Version 1.0.2 at 2013-05-22

  1. Update progress bar at launch time.

  2. Fix bug that war file clean feature only works well when tomcat is online.

  3. Add support for static.war in both build and clean page.

  4. Add support for one-key widget tool in build page.

  5. Fix bug that sometimes error "connection refused" occrus during launch time.

  6. Fix bug that error when builing a war file.

  7. Fix bug that error in console when clean items in clean page.

  8. Fix bug that deploy incorrect war file in category tab under build page.

  9. Fix bug that notification feature doesn't work after a lot of frequent notifications displayed at a glance.

  10. Optimize UX that clean/refresh button should be greyed out when operation is on-going.

  11. Add portal-testsuites-offlinepvr-war support in build page.



Version 1.0.1 at 2013-05-13

  1. Remove splash form with a progress bar instead.
  
  2. Add version number visible on main page.

  3. Refactor the build list in build page.

  4. Make build, deploy, test separately, so it's possible to only execute one of them.

  5. Remove animation from tab switch.

  6. Fix bug that error occures during launch time if portal-team repository path is not a correct path in config file.

  7. Update the input text hint in deploy, setting pages.

  8. Remove the auto hide feature.

  9. Fix bug that sometimes reboot computer is needed to solve "ERROR:Failed to start server!".

  10. Add pre-check to "create a desktop icon".

  11. Add new feature to prevent a new installation if you already have one on your computer.

  12. Add new feature that if you don't have your settings full filled, ThePower will go to setting page as default.

  13. Add new feature that you can't uninstall ThePower if it is running.

  14. Add new feature that uninstaller will ask end-user for deleting user profile.

  15. Add new feature that make sure you can only have one instance of thepower running at your computer at once.
