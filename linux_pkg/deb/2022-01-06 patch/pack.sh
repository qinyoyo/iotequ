#/bin/bash
chmod 755 deb_files/DEBIAN/p*
chmod 755 deb_files/tmp/svaspatch0106/svas.jar
dpkg -b deb_files/ svaspatch0106.deb
cat deb_files/DEBIAN/control


