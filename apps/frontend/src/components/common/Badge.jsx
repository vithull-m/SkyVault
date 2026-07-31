import React from 'react';

const Badge = ({ type = 'info', children }) => {
  const badgeClass = `badge badge-${type}`;
  return <span className={badgeClass}>{children}</span>;
};

export default Badge;
