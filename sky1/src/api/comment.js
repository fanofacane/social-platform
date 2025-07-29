import request from "@/utils/request";
export const getPostCommentsApi = (postId, userId) => { return request.get(`/posts/comments/${postId}`, { params: { userId } }); }

export const insertCommentApi = (postId, content, parentCommentId) => { return request.post(`/comments`, { postId, content, parentCommentId }); }