##### HEADER SECTION #####

Name:           svas
Version:        3.2.0
Release:        1016
Summary:        Rpm package for Svas

License:        GPL
URL:            http://www.svein.com.cn
Source0:        %{name}.jar
Source1:				%{name}.so
Source2:        iotequ.yml
Source3:				%{name}.sql
Source4:				%{name}.service
Requires:       shadow-utils,bash
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

# create Spring Starter service group
getent group svas >/dev/null || groupadd -f -g 30000 -r svas

# create Spring Starter service user
if ! getent passwd svas >/dev/null ; then
    if ! getent passwd 30000 >/dev/null ; then
      useradd -r -u 30000 -g svas -d /home/svas -s /sbin/nologin -c "svas service account" svas
    else
      useradd -r -g svas -d /home/svas -s /sbin/nologin -c "svas service account" svas
    fi
fi
exit 0

##### INSTALL SECTION #####
%install

app_dir=%{buildroot}/usr/local/%{name}
service_dir=%{buildroot}/etc/systemd/system
#service_dir=%{buildroot}/%{_unitdir}


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

cp %{SOURCE4} $service_dir

##### FILES SECTION #####
%files

# define default file attributes
%defattr(0755,svas,svas,-)

# list of directories that are packaged
%dir /usr/local/svas

# list of files that are packaged
/usr/local/svas/svas.jar
/usr/local/svas/svas.so
/usr/local/svas/svas.sql
/usr/local/svas/iotequ.yml

/etc/systemd/system/%{name}.service
#   /usr/lib/systemd/system/svas.service

##### POST INSTALL SECTION #####
%post
echo Initial... 
echo Please input password of root for MYSQL, then edit the web service config
mysql -uroot --host=127.0.0.1 -p < /usr/local/svas/svas.sql
vi /usr/local/svas/iotequ.yml

# ensure svas service is enabled and running
# %systemd_post %{name}.service
# %{_bindir}/systemctl enable %{name}.service
ln -s /etc/systemd/system/svas.service /etc/systemd/system/multi-user.target.wants/svas.service
%{_bindir}/systemctl start %{name}.service

##### UNINSTALL SECTION #####
%preun

# ensure Spring Starter service is disabled and stopped
%systemd_preun %{name}.service

%postun

case "$1" in
	0) # This is a package remove

		# remove app and data folders
		systemctl stop svas
		rm -rf /usr/local/svas
		rm /etc/systemd/system/multi-user.target.wants/svas.service
		rm /etc/systemd/system/svas.service 
		# remove svas service user and group
		userdel svas
		groupdel svas
	;;
	1) # This is a package upgrade
		# do nothing
	;;
esac

# ensure svas service restartet if an upgrade is performed
%systemd_postun_with_restart %{name}.service

##### CHANGELOG SECTION #####
%changelog
