import request from "@/utils/request";
export const getAllProduct = () => { return request.get(`/product`); };

export const buyProduct = (userId, productId, numbers) => { return request.post(`/order`, { userId, productId, numbers }); };

export const getOrderInfo = (userId) => { return request.get(`/order/getOrderInfo`, { params: { userId } }); };

export const confirmrOrders = (orderId) => { return request.post(`/order/confirmReceive/${orderId}`); };
