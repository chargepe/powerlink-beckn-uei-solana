import axios, { AxiosRequestConfig } from 'axios';
import { baseUrl } from './endpoint';

const apiInstance = async ({
  endpoint,
  method = 'GET',
  body = null,
}: {
  endpoint: string;
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE';
  body?: any;
}) => {
  try {

    const config: AxiosRequestConfig = {
      url: baseUrl + endpoint,
      method: method || 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
      data: body ? JSON.stringify(body) : null,
    };

    const response = await axios(config);

    return response.data;
  } catch (error: any) {
    if (error.response) {
      throw {
        message: error.response.data.message || 'Unknown error occurred',
        code: error.response.data.code || 'UNKNOWN_ERROR',
      };
    } else {
      throw { message: 'Something went wrong', code: 'BAD_REQUEST' };
    }
  }
};

export default apiInstance;
