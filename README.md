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
* Browsing FTP files (this only partially works, I talk about this more below)
* Previewing files (plain/text, image/jpeg, image/png)
* Opening files (Any supported by the operating system)
* Extracting archive files (application/zip)

To browse files:
* Use either the arrow keys or mouse to navigate the file tree
* N.B., directory refreshing has not been implemented. If you wish to see changes in the file tree,
  you will need to open a new tab.

To open/close new tabs:
* Use CMD/CTRL+t and CMD/CTRL+w to open/close tabs respectively.

To browse FTP files:
* Open a new FTP connection dialog (Under "File", "New FTP Tab", or CMD/CTRL+y)
* Enter in the host, username and password, click "Ok". This will open a new FTP tab and load the
  FTP file system. N.B., it is extremely slow and does not work that well, I have left this in as a
  "demo" of how I would like to implement FTP file browsing, but I would not call this usable yet.

To preview files:
* Select a file in the file tree
* Press space

To open files:
* Select a file in the file tree
* Press enter. This will automatically open the file using the default associated application unless
  it is a ZIP file.

To extract archive files:
* Select a ZIP file in the file tree
* Press enter. This will automatically extract the ZIP file into the current folder. N.B., this
  won't trigger a refresh on the file tree, you will need to do this yourself.

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
* Support better shortcuts (tabbing between tabs, space to close preview mode, keys to navigate file
  tree while in preview mode)
* Display relevant errors to the user (e.g., if something goes wrong with unarchiving a file, or
  previewing a file)
* Ensure interaction with the application is documented and all shortcuts can be found and learnt
* Dynamically refresh the file tree when the file system changes
* Re-use view model results for tabs that refer to the same file system
* Ensure FTP works and is efficient
* Ensure all potentially long-running actions are backgrounded and have progress bars/loading
  images. Currently there are a number of calls to `Files` which, depending upon the underlying file
  system, could be quite expensive.
* Ensure FTP can preview/open/extract files
