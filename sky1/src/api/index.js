import request from "@/utils/request";
export const queryAllCardApi = (userId, fileType, cursor, size) => { return request.get('/posts', { params: { userId, fileType, cursor, size } }); }
export const queryAllCardDetailApi = (id, userId) => { return request.get(`/posts/${id}`, { params: { userId } }); }
export const seachCardByKeyApi = (key, userId) => { return request.get(`/posts/all`, { params: { key, userId } }); };

export const toggleLikeApi = (userId, postId, commentId) => { return request.post(`/likes/toggle`, { userId, postId, commentId }) }

export const toggleCollectionApi = (userId, postId) => { return request.post(`/collection/toggle`, { userId, postId }) }

