
function riderAcceptOrder(data) {
    return $axios({
        'url': '/rider/accept',
        'method': 'post',
        data
    })
}

//查询我接收的订单
function selectMyApi() {
    return $axios({
        'url': '/rider/list',
        'method': 'get',
    })
}
