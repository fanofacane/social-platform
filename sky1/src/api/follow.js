import request from "@/utils/request";
export const followUserApi = (followerId, targetUserId) => { return request.post(`/users/${followerId}/followings/${targetUserId}`); }
export const unFollowUserApi = (followerId, targetUserId) => { return request.delete(`/users/${followerId}/unfollow/${targetUserId}`); }
export const checkFollowerApi = (followerId, targetUserId) => { return request.get(`/users/${followerId}/checkfollowers/${targetUserId}`); }
export const getFollowerApi = () => { return request.get(`/users/followings`); }

// export const getPostsByUsersApi = () => { return request.get(`/users/dynamics`); }
export const getPostsByUsersApi = (lastTimestamp) => { return request.get(`/users/dynamics`, { params: { lastTimestamp } }); }
