export const formatNumber = (num, decimals = 2) => {
  if (num === null || num === undefined || isNaN(num)) return 'N/A';
  return Number(num).toLocaleString('en-US', {
    minimumFractionDigits: decimals,
    maximumFractionDigits: decimals,
  });
};

export const formatTimestamp = (timestampStr) => {
  if (!timestampStr) return 'N/A';
  try {
    const date = new Date(timestampStr);
    return date.toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
      hour12: false,
    });
  } catch (e) {
    return timestampStr;
  }
};

export const truncateHash = (hash, startChars = 6, endChars = 4) => {
  if (!hash) return '';
  if (hash.length <= startChars + endChars) return hash;
  return `${hash.substring(0, startChars)}...${hash.substring(hash.length - endChars)}`;
};
