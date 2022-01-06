# !/bin/sh
chmod 755 dpkg/DEBIAN/p*
chmod 755 dpkg/usr/local/svas/svas.jar
dpkg -b deb_files svas3.2.0_x64.deb