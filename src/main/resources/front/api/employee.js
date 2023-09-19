
function employeeListApi() {
    return $axios({
        'url': '/employee/list',
        'method': 'get'
    })
}