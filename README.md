## Overview

[Logstash](https://github.com/elastic/logstash) is an open source, server-side data processing pipeline. It has a pluggable framework featuring over 200 plugins. The self-defined [logstash-input-pravega](https://github.com/pravega/logstash-input-pravega) and  [logstash-output-pravega](https://github.com/pravega/logstash-output-pravega) will be used with logstash filter plugins to transform unstructured log to json. 
The input plugin reads stream from pravega. Then the filter plugin transforms the unstructed log to json with self-defined rules. The filter plugin only process the logs which matches the rules. Otherwise, it will be skipped. Finally, the json data will be writen to pravage by the writer plugin. Here is the process pipeline.
![pipeline](/home/yaol7/Downloads/pipeline.png  "pipeline"). 

Currently, the configed filter plugin can support for the apachelog/syslog/nginx/mysql query and error log, custom application log.


## Usage
- First, have logstash installed. 
```sh
wget https://artifacts.elastic.co/downloads/logstash/logstash-6.7.0.tar.gz
tar -xzvf logstash-6.7.0.tar.gz
```
- Get the pravega.json file from this repo
```sh
git clone https://github.com/pravega/pravega-search.git
sudo mv ~/pravega-search/tools/logstash-plugin/ logstash-6.7.0/
```
- Have the [logstash-input-pravega](https://github.com/pravega/logstash-input-pravega) and the [logstash-output-pravega](https://github.com/pravega/logstash-output-pravega) installed.  Follow the README.md to install it.
- Config the input/output plugin in ~/pravega-search/tools/logstash-plugin/pravega.conf. 
```
# how to config the input plugin
e.g.
input {
    pravega {
      pravega_endpoint => "tcp://<host>:<port>"
      stream_name => "myStream"
    }
  }
}

  # other optional configs. When the controller authorization of pravega is open, the usename and password is required.

  scope:             default 'global'
  reader_threads:    default 1
  read_timeout_ms:   default 60000
  username:          default ""
  password:          default ""

```
```
# how to config the output plugin
e.g.
output {
    pravega {
      pravega_endpoint => "tcp://<host>:<port>"
      stream_name => "myStream"
    }
  }
}
  # other optional configs. When the controller authorization of pravega is open, the usename and password is required.

      scope:             default: 'global'
      num_of_segments:   default: 1
      routing_key:       default: ""
      username:          default: ""
      password:          default: ""
```
**Note**

 When the stream_name of input is the same as the stream_name of output, the new stream_name is created by the output plugin. The main purpose is to avoid the endless loop. You can find the new created stream_name in logstash console log.

- Start the logstash pipeline
```
~/logstash-6.7.0$: sudo bin/logstash -f logstash-plugin/pravega.conf
```