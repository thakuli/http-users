# How to use OpenShift s2i leiningen builder 

Create builder image:
# sudo docker build --tag leiningen-builder .

Tag builder image:
#sudo docker tag leiningen-builder epor.netact.nsn-rdnet.net:5000/openshift3/leiningen-builder

Push builder image to OpenShift docker registry
#  sudo docker push epor.netact.nsn-rdnet.net:5000/openshift3/leiningen-builder

Create Builder ImageStream
# oc login 
# oc create -f leiningen-builder-is.json

expose service:
# oc expose dc hopohopo --name=hopohopo-svc  --port=8080 

Create route
# Console used

FIXME: description

## Installation

Download from http://example.com/FIXME.

## Usage

FIXME: explanation

    $ java -jar http-users-0.1.0-standalone.jar [args]

## Options

FIXME: listing of options this app accepts.

## Examples

...

### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
