systemctl stop svas
mysql --host=127.0.0.1 --user=root --password=Passw0rd@Jw2021 < patch.sql
cp svas.jar /usr/local/svas
chown -f svas:svas /usr/local/svas/svas.jar
chmod 755 /usr/local/svas/svas.jar
systemctl start svas
