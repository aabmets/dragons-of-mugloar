export const formatThousands = (num: number) => {
  const s = Math.abs(num).toString().split('.');
  const g = s[0].replace(/\B(?=(\d{3})+(?!\d))/g, ' ');
  return (num < 0 ? '-' : '') + g + (s[1] ? '.' + s[1] : '');
};
