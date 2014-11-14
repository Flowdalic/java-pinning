#!/usr/bin/env bash

if [[ ! -f "$1" ]]; then
	echo "Usage: $0 <certificate_path>"
	exit 1
fi

PIN=$(openssl x509 -in "$1" -noout -fingerprint -sha256)
if [[ $? -ne 0 ]]; then
	exit $?
fi
PIN=${PIN#*=}
PIN=${PIN//:/}

echo "CERTSHA256:"$PIN
