import request from "@/utils/request";
export const queryUserApi = (id) => { return request.get(`/user/info`, { params: { id } }); }
export const queryVisitorUserApi = (id) => { return request.get(`/user/visitor`, { params: { id } }); }
export const querCardByIdApi = (id) => { return request.get(`/user/card/${id}`); }

export const querVisitorCardByIdApi = (id) => { return request.get(`/user/visitor/card/${id}`); }
export const followUserApi = (id) => { return request.post(`/user/followings`, id); }

export const querLikedCardByIdApi = () => { return request.get(`/likes/getUserLike`); }

export const querCollectionCardByIdApi = () => { return request.get(`/collection/getUserCollection`); }

export const updateUserinfoApi = (info) => { return request.put(`user/updateInfo`, info) }

export const insertPost = (postForm) => { return request.post(`/user/publicPost`, postForm) }
