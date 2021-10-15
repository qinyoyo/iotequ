groupadd svas
useradd  svas -g svas
passwd -d svas
vi iotequ.yml
mkdir /usr/local/svas
chown svas:svas /usr/local/svas
cp * /usr/local/svas
chown svas:svas /usr/local/svas/*
chmod +755 /usr/local/svas/svas
chmod +755 /usr/local/svas/*.sh
chmod +755 /usr/local/svas/svas.jar
cp /usr/local/svas/svas /etc/init.d/svas
mysql --host=127.0.0.1 -uroot -proot < svas.sql

