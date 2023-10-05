import axios from "~/axios"
export function getImageClassList(page){
  return axios.get("/admin/image_class/"+page+"?limit=10")
}
export function createImageClass(data){
  return axios.post("/admin/image_class",data)
}
export function updateImageClass(id,data){
  return axios.post("/admin/image_class/"+id,data)
}
export function deleteImageClass(id,data){
  return axios.post(`/admin/image_class/${id}/delete`)
}
export function getImageList(id,page=1){
  return axios.get(`/admin/image_class/${id}/image/${page}`)
}