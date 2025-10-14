import type * as t from '@/types'
import * as c from '@/const'

export function formatThousands(num: number) {
  const s = Math.abs(num).toString().split('.');
  const g = s[0].replace(/\B(?=(\d{3})+(?!\d))/g, ' ');
  return (num < 0 ? '-' : '') + g + (s[1] ? '.' + s[1] : '');
}

export function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

export function isTrapAdvert(ad: t.Advertisement) {
    return ad
        && ad.message?.toLowerCase().includes('steal')
        && c.PROBABILITIES.indexOf(ad.probability) <= 2
        && ad.reward > 120;
}
