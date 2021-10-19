##### HEADER SECTION #####

Name:           svas
Version:        3.2.0
Release:        1016
Summary:        Rpm package for Svas service

License:        GPL
URL:            http://www.svein.com.cn
Source0:        %{name}.jar
Source1:	%{name}.so
Source2:        %{name}.yml
Source3:	%{name}.sql
Source4:	%{name}.service
Source5:	%{name}.init.d
Requires:       mysql-community-server >= 5.7, java-1.8.0-openjdk
BuildRequires:	systemd
%{?systemd_requires}

BuildArch:      x86_64

%description
%{summary}

# disable debuginfo, which is useless on binary-only packages
%define debug_package %{nil}

# do not repack jar files
%define __jar_repack %{nil}

##### PREPARATION SECTION #####
%prep

# empty section

##### BUILD SECTION #####
%build

# empty section

##### PREINSTALL SECTION #####
%pre

# create %{name} service group and user
id -g %{name} >/dev/null 2>&1
if [ $? -ne 0 ];then
	groupadd -f -r %{name}
fi
id -u %{name} >/dev/null 2>&1
if [ $? -ne 0 ];then
	useradd -r -g %{name} --no-create-home -s /sbin/nologin -c "%{name} service account" %{name}
fi

##### INSTALL SECTION #####
%install

app_dir=%{buildroot}/usr/local/%{name}
service_dir=%{buildroot}/%{_unitdir}

# cleanup build root
rm -rf %{buildroot}
mkdir -p  %{buildroot}

# create app folder
mkdir -p $app_dir

# create service folder
mkdir -p $service_dir

# copy all files
cp %{SOURCE0} $app_dir/
cp %{SOURCE1} $app_dir/
cp %{SOURCE2} $app_dir/
cp %{SOURCE3} $app_dir/
cp %{SOURCE5} $app_dir/

cp %{SOURCE4} $service_dir

##### FILES SECTION #####
%files

# define default file attributes
%defattr(0755,%{name},%{name},-)

# list of directories that are packaged
%dir /usr/local/%{name}

# list of files that are packaged
/usr/local/%{name}/%{name}.jar
/usr/local/%{name}/%{name}.so
/usr/local/%{name}/%{name}.sql
/usr/local/%{name}/%{name}.init.d
/usr/local/%{name}/%{name}.yml

/%{_unitdir}/%{name}.service

##### POST INSTALL SECTION #####
%post
echo Initial... 
exec 6<&0 0</dev/tty
echo -n Please input the web service port:
read portOfService
echo -n Please input password of root for MYSQL:
read passwordOfRoot
sed -i 's/port\s*:\s*12345/port : '"$portOfService"'/' /usr/local/%{name}/%{name}.yml
sed -i 's/password\s*:\s*root/password : '"$passwordOfRoot"'/g' /usr/local/%{name}/%{name}.yml
mysql -uroot --host=127.0.0.1 -p$passwordOfRoot < /usr/local/%{name}/%{name}.sql

systemctl status >/dev/null 2>&1
if [ $? -eq 0 ]; then
	ln -s /%{_unitdir}/%{name}.service /etc/systemd/system/multi-user.target.wants/%{name}.service
	systemctl start svas.service
else
	cp /usr/local/svas/svas.init.d /etc/init.d/svas
	service svas start
fi

##### UNINSTALL SECTION #####
%preun
systemctl status >/dev/null 2>&1
if [ $? -eq 0 ]; then
	systemctl stop %{name}
	rm -f /etc/systemd/system/multi-user.target.wants/%{name}.service
else
	service svas stop
	rm -f /etc/init.d/svas
fi

%postun
case "$1" in
	0) # This is a package remove
	rm -rf /usr/local/%{name}

	id -u %{name} >/dev/null 2>&1
	if [ $? -eq 0 ];then
		userdel -f %{name}
	fi
	id -g %{name} >/dev/null 2>&1
	if [ $? -eq 0 ];then
		groupdel -f %{name}
	fi

	echo %{name}-%{version} removed!
	;;
	1) # This is a package upgrade
		# do nothing
	;;
esac

##### CHANGELOG SECTION #####
%changelog
