import React, { useState, useEffect } from 'react';
import { Table, Card, Modal, Button, message, Space } from 'antd';
import { DownloadOutlined, EyeOutlined } from '@ant-design/icons';
import { fetchAlertRecords, fetchAccessRecordsByEmployee } from '../services/api';
import axiosInstance from '../utils/axiosConfig';

const AlertRecords = () => {
  const [alertRecords, setAlertRecords] = useState([]);
  const [loading, setLoading] = useState(true);
  const [accessRecords, setAccessRecords] = useState([]);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedEmployee, setSelectedEmployee] = useState(null);
  const [accessLoading, setAccessLoading] = useState(false);
  const [exportLoading, setExportLoading] = useState(false);

  // 导出Excel
  const exportToExcel = async () => {
    try {
      setExportLoading(true);
      const response = await axiosInstance.get('/alerts/export/excel', {
        responseType: 'blob',
      });
      
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'alert_records.xlsx');
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
      message.success('导出成功');
    } catch (error) {
      message.error('导出失败');
    } finally {
      setExportLoading(false);
    }
  };

  // 获取提醒记录
  useEffect(() => {
    const loadAlertRecords = async () => {
      try {
        setLoading(true);
        const data = await fetchAlertRecords();
        setAlertRecords(data);
      } catch (error) {
        message.error('获取提醒记录失败');
      } finally {
        setLoading(false);
      }
    };

    loadAlertRecords();
  }, []);

  // 查看原始门禁记录
  const viewAccessRecords = async (employeeId) => {
    try {
      setAccessLoading(true);
      const data = await fetchAccessRecordsByEmployee(employeeId);
      setAccessRecords(data);
      setSelectedEmployee(employeeId);
      setIsModalVisible(true);
    } catch (error) {
      message.error('获取门禁记录失败');
    } finally {
      setAccessLoading(false);
    }
  };

  // 关闭模态框
  const handleCancel = () => {
    setIsModalVisible(false);
    setSelectedEmployee(null);
    setAccessRecords([]);
  };

  // 提醒记录表列配置
  const alertColumns = [
    {
      title: '员工ID',
      dataIndex: 'employeeId',
      key: 'employeeId',
      width: 120,
      sorter: (a, b) => a.employeeId.localeCompare(b.employeeId),
    },
    {
      title: '提醒日期',
      dataIndex: 'alertDate',
      key: 'alertDate',
      width: 150,
      render: (date) => new Date(date).toLocaleDateString(),
      sorter: (a, b) => new Date(a.alertDate) - new Date(b.alertDate),
    },
    {
      title: '提醒时间',
      dataIndex: 'alertTime',
      key: 'alertTime',
      width: 120,
    },
    {
      title: '提醒消息',
      dataIndex: 'alertMessage',
      key: 'alertMessage',
      ellipsis: true,
    },
    {
      title: '创建时间',
      dataIndex: 'createdAt',
      key: 'createdAt',
      width: 180,
      render: (date) => new Date(date).toLocaleString(),
      sorter: (a, b) => new Date(a.createdAt) - new Date(b.createdAt),
    },
    {
      title: '操作',
      key: 'action',
      width: 120,
      render: (_, record) => (
        <Button type="primary" size="small" onClick={() => viewAccessRecords(record.employeeId)}>
          查看原始记录
        </Button>
      ),
    },
  ];

  // 门禁记录表列配置
  const accessColumns = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: 80,
    },
    {
      title: '员工ID',
      dataIndex: 'employeeId',
      key: 'employeeId',
      width: 120,
    },
    {
      title: '访问时间',
      dataIndex: 'accessTime',
      key: 'accessTime',
      width: 180,
      render: (time) => new Date(time).toLocaleString(),
    },
    {
      title: '进出方向',
      dataIndex: 'direction',
      key: 'direction',
      width: 100,
      filters: [
        { text: '进入', value: 'IN' },
        { text: '离开', value: 'OUT' },
      ],
      onFilter: (value, record) => record.direction === value,
    },
    {
      title: '创建时间',
      dataIndex: 'createdAt',
      key: 'createdAt',
      width: 180,
      render: (time) => new Date(time).toLocaleString(),
    },
  ];

  return (
    <Card 
      title="提醒记录" 
      className="records-card"
      extra={
        <Button 
          type="primary" 
          onClick={exportToExcel} 
          loading={exportLoading}
          icon={<DownloadOutlined />}
        >
          导出Excel
        </Button>
      }
    >
      <Table
        columns={alertColumns}
        dataSource={alertRecords}
        rowKey="id"
        loading={loading}
        pagination={{
          pageSize: 10,
          showSizeChanger: true,
          showTotal: (total) => `共 ${total} 条记录`,
        }}
        scroll={{ x: 800 }}
      />

      <Modal
        title={`员工 ${selectedEmployee} 的门禁记录`}
        open={isModalVisible}
        onCancel={handleCancel}
        footer={[
          <Button key="back" onClick={handleCancel}>
            关闭
          </Button>,
        ]}
        width={800}
      >
        <Table
          columns={accessColumns}
          dataSource={accessRecords}
          rowKey="id"
          loading={accessLoading}
          pagination={{
            pageSize: 8,
            showSizeChanger: true,
          }}
          scroll={{ x: 600 }}
        />
      </Modal>
    </Card>
  );
};

export default AlertRecords;