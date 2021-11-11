##### HEADER SECTION #####

Name:           svas
Version:        3.2.0
Release:        1111
Summary:        Rpm package for svas web service

License:        GPL
URL:            http://www.svein.com.cn
Source0:        %{name}.jar
Source1:	%{name}.so
Source2:        %{name}.yml
Source3:	%{name}.sql
Source4:	%{name}.service
Source5:	%{name}.init.d
# Requires:       mysql-community-server >= 5.7, java-1.8.0-openjdk     
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

REQ=0
if [ -f /lib64/libmysqlclient.so.20 ]; then 
   REQ=1
else 
   echo Warning!!! File /lib64/libmysqlclient.so.20 not found! Need install mysqllient 5.7 or mysql server 5.7.
   echo            If them installed, check you libaries seek path. 
fi
java -version 2>&1| grep -i -E "(openjdk|openjre|java).*version.*1\.8"
if [ $? -eq 0 ]; then
   if [ $REQ -eq 1 ]; then 
      REQ=2
   fi
else
   echo Warning!!! Java 1.8.x or openjdk 1.8.x not found.
fi

echo Please config your database and svas 
echo like: sed -i 's/port\s*:\s*12345/port : '"$portOfService"'/' /usr/local/%{name}/%{name}.yml
echo       sed -i 's/password\s*:\s*root/password : '"$passwordOfRoot"'/g' /usr/local/%{name}/%{name}.yml
echo       mysql -uroot --host=127.0.0.1 -p$passwordOfRoot < /usr/local/%{name}/%{name}.sql

systemctl status >/dev/null 2>&1
if [ $? -eq 0 ]; then
	ln -s /%{_unitdir}/%{name}.service /etc/systemd/system/multi-user.target.wants/%{name}.service
	if [ $REQ -eq 2 ]; then 
	   echo To start the service, systemctl start %{name}
	fi
else
	cp /usr/local/%{name}/%{name}.init.d /etc/init.d/%{name}
	if [ $REQ -eq 2 ]; then
	   service %{name} start
	fi
fi

##### UNINSTALL SECTION #####
%preun
systemctl status >/dev/null 2>&1
if [ $? -eq 0 ]; then
	systemctl stop %{name}
	rm -f /etc/systemd/system/multi-user.target.wants/%{name}.service
else
	service %{name} stop
	rm -f /etc/init.d/%{name}
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
