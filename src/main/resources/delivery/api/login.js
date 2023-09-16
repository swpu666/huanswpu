function loginApi(data) {
    return $axios({
      'url': '/delivery/login',
      'method': 'post',
      data
    })
}

function sendMsgApi(data) {
    return $axios({
        'url': '/delivery/sendMsg',
        'method': 'post',
        data
    })
}

function loginoutApi() {
  return $axios({
    'url': '/delivery/loginout',
    'method': 'post',
  })
}

  