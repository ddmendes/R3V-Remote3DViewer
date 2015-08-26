#! /bin/sh
#/etc/init.d/resolvconf.sh

case "$1" in
	start)
		echo "Starting resolvconf "
		cp /etc/conf/resolv.conf /etc/resolv.conf
		;;
	stop)
		echo "Stopping resolvconf "
		;;

esac

exit 0
