# bowser

[![Project Status: WIP – Initial development is in progress, but there has not yet been a stable, usable release suitable for the public.](http://www.repostatus.org/badges/latest/wip.svg)](http://www.repostatus.org/#wip)

A file browser for viewing the system and FTP file trees.

## Building, runing and testing the code

This project uses the Gradle build system, with a wrapper.
To build, run and test the project, use the following commands:
* `./gradlew build`
* `./gradlew run`
* `./gradlew test`

## Running the application

Technically speaking the application is system-independent, however I have only had a chance to test
    it on a Mac OS.
If there are any issues with Windows or Linux, please let me know and I can try fix them up.

The following features are implemented:
* Browsing files (with tabs)
* Browsing archive files (application/zip)
* Browsing FTP servers
* Previewing files (text/\*, image/jpeg, image/png)
* Opening files (Any supported by the operating system)

To browse files:
* Use the arrow keys and either enter, or the mouse, to navigate into a directory
* Use backspace to navigate up to the parent directory

To open/close new tabs:
* Use CMD/CTRL+t and CMD/CTRL+w to open/close tabs respectively

To browse archive files:
* Open the archive file just as you would a regular directory

To browse FTP servers:
* Open a new FTP connection dialog (Under "File", "New FTP Tab", or CMD/CTRL+y)
* Enter in the host, username and password, click "Ok". This will open a new FTP tab and load the
  FTP file system. N.B., it is rather slow depending on the target FTP server, and I was unable to
  properly use it for a local FTP server. There are also some features the library does not support
  which makes some of the experience less than ideal.

To preview files:
* Select a file in the file tree (it will preview in the split pane on the right)

To open files:
* Select a file in the file tree
* Press enter. This will automatically open the file using the default associated application.

## My thoughts

What I like:
* The architecture. I find it easy to add new features, or support new file types for
  extraction/preview. I particularly like how Java7's new FileSystem library works and
  that it is fairly easy to use to help support a generic file browser.
* Swing architecture. I haven't used Swing in quite a few years, so I struggled initially on
  determining the best way to architect the different components. I went down an MVVM route where a
  Builder helps to set up the actual Swing components, then a View deals with interactions and a
  ViewModel deals with complex operations with the data model. I am quite happy with how this has
  turned out as it feels like the components are quite de-coupled and easy to work with
  individually.

What I struggled with:
* FTP. I am not hugely familiar with all the details of FTP, so I found it quite difficult to try
  and debug when the connections either did not work (when testing with a local FTP server) or were
  extremely slow (when testing with a public, remote FTP server). As such, I decided not to spend a
  huge amount of time trying to figure it out as I felt it was more important to get the rest of the
  application working first.
* TDD. Traditionally I have tried to use a TDD approach, however given I was experimenting and
  refactoring quite a bit during this project I decided it would be best to leave testing until
  last.

Future features I would add before I consider this production ready:
* Support better shortcuts (e.g., tabbing between tabs)
* Display relevant errors to the user (while an error is displayed to the user, it would be nice to
  provide them with more information than just "Something went wrong")
* Ensure interaction with the application is documented and all shortcuts can be found and learnt
* Dynamically refresh the current directory when the file system changes
* Re-use view model results for tabs that refer to the same file system
* Ensure all potentially long-running actions have progress bars/loading images.
* Ensure FTP works well and is efficient
* Ensure FTP can open files
