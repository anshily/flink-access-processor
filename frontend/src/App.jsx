import React, { useState } from 'react';
import { Layout, Tabs, Switch, Typography, Space } from 'antd';
import { BulbOutlined, BulbFilled, AlertOutlined, CalendarOutlined, ClockCircleOutlined } from '@ant-design/icons';
import { ThemeProvider, useTheme } from './context/ThemeContext';
import AlertRecords from './components/AlertRecords';
import ConsecutiveWorkDays from './components/ConsecutiveWorkDays';
import './App.css';

const { Header, Content } = Layout;
const { Title } = Typography;

const AppContent = () => {
  const { isDark, toggleTheme } = useTheme();
  const [activeTab, setActiveTab] = useState('alerts');

  return (
    <Layout className="app-layout">
      <Header className="app-header">
        <Space direction="vertical" size="middle" style={{ width: '100%' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <Title level={3} style={{ color: 'white', margin: 0 }}>
              Flink 监控系统
            </Title>
            <Space>
              {isDark ? <BulbFilled style={{ color: 'white' }} /> : <BulbOutlined style={{ color: 'white' }} />}
              <Switch
                checked={isDark}
                onChange={toggleTheme}
                checkedChildren="暗黑"
                unCheckedChildren="明亮"
              />
            </Space>
          </div>
        </Space>
      </Header>
      <Content className="app-content">
        <Tabs
          activeKey={activeTab}
          onChange={setActiveTab}
          items={[
            {
              key: 'alerts',
              label: (
                <span>
                  <AlertOutlined />
                  提醒记录
                </span>
              ),
              children: <AlertRecords />,
            },
            {
              key: 'consecutive',
              label: (
                <span>
                  <CalendarOutlined />
                  连续工作天数
                </span>
              ),
              children: <ConsecutiveWorkDays />,
            },
          ]}
        />
      </Content>
    </Layout>
  );
};

function App() {
  return (
    <ThemeProvider>
      <AppContent />
    </ThemeProvider>
  );
}

export default App;
