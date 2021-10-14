groupadd svasgroup
useradd  svasuser -g svasgroup
passwd -d svasuser
vi iotequ.yml
mkdir /usr/local/svas
chown svasuser:svasgroup /usr/local/svas
cp * /usr/local/svas
chown svasuser:svasgroup /usr/local/svas/*
chmod +755 /usr/local/svas/svas
chmod +755 /usr/local/svas/*.sh
chmod +755 /usr/local/svas/svas.jar
cp /usr/local/svas/svas /etc/init.d/svas

