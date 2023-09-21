//提交订单
function  addOrderApi(data){
    return $axios({
        'url': '/order/submit',
        'method': 'post',
        data
      })
}

//查询所有订单
function orderListApi() {
  return $axios({
    'url': '/order/list',
    'method': 'get',
  })
}

//分页查询订单
function orderPagingApi(data) {
  return $axios({
      'url': '/order/userPage',
      'method': 'get',
      params:{...data}
  })
}

//再来一单
function orderAgainApi(data) {
  return $axios({
      'url': '/order/again',
      'method': 'post',
      data
  })
}
// 查询未被接单的订单

function orderReceive() {
  return $axios({
      'url': '/order/receive',
      'method': 'get',
  })
}

function deliveryReceive(data) {
  return $axios({
      'url': '/order/receive',
      'method': 'post',
      data
  })
}