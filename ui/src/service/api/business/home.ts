import { request } from '@/service/request';

export function fetchHomeStats(params?: { beginDate?: string; endDate?: string }) {
  return request<Api.Business.HomeStats>({
    url: '/business/home/stats',
    method: 'get',
    params
  });
}
