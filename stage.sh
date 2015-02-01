#!/bin/bash

base_dir=$(cd $(dirname $0); pwd)"/target/universal/stage"
mem=512

bash ${base_dir}/bin/qiita-playframework-trace-log -mem $mem
