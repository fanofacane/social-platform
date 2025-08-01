import request from "@/utils/request";
import aiRequest from "@/utils/airequest";
import axios from "axios";
export const queryChatUserList = (id) => { return request.get(`/list`, { params: { id } }); }

export const queryChatList = (id) => { return request.get(`/list/talk`, { params: { id } }); }

export const clearUnread = (id) => { return request.post(`/list/clearUnread/${id}`); }

// export const sendAI = (searchText, userId) => { return axios.get(`http://localhost:8091/ai/answer/sse`, { params: { searchText, userId } }); }
//mcp接口
export const sendAI = (searchText, userId) => { return aiRequest.get(`/ai/answer/sse`, { params: { searchText, userId } }); }
// export const sendAI = (msg, userId) => { return request.get(`/list/chat`, { params: { msg, userId } }); }
