import request from "@/utils/request";
export const loginApi = (data) => { return request.post(`/login`, data); };

export const registerApi = (data) => { return request.post(`/register`, data); };


export const logout = (userId) => { return request.post(`/logout/${userId}`); };



