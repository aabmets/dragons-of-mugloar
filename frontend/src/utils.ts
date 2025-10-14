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

export function isStealing(ad: t.Advertisement) {
    return !!ad.message?.toLowerCase().includes('steal')
}

export function isTrapAdvert(ad: t.Advertisement) {
    if (typeof ad.trapDetected === 'boolean') {
        return ad.trapDetected;
    }
    const easy = c.PROBABILITIES.indexOf(ad.probability) <= 2
    const riches = ad.reward > 120

    ad.trapDetected = isStealing(ad) && easy && riches
    return ad.trapDetected
}

export function diffMessageBoards(oldVal: t.MessageBoard, newVal: t.MessageBoard): t.MsgBoardDiff {
  const oldIds: Set<string> = new Set(oldVal.map(ad => ad.adId));
  const newIds: Set<string> = new Set(newVal.map(ad => ad.adId));

  const add: t.MessageBoard = newVal.filter(ad => !oldIds.has(ad.adId));
  const remove: t.MessageBoard = oldVal.filter(ad => !newIds.has(ad.adId));
  const keep: t.MessageBoard = oldVal.filter(ad => newIds.has(ad.adId));

  return { add, remove, keep };
}

export function maybeReplaceMessage(oldVal: t.MessageBoard, newVal: t.MessageBoard, aiMessages: string[]) {
  const diff = diffMessageBoards(oldVal, newVal)

  if (diff.add.length == 1) {
    const ad = diff.add[0]
    const match = !isTrapAdvert(ad) && !isStealing(ad) && aiMessages.length > 0
    if (match) {
      const aiMsg = aiMessages.pop()
      console.log(`Replacing original message with AI message:\n'${ad.message}'\n=>\n'${aiMsg}'\n`)
      ad.message = aiMsg
    }
    ad.hasAiMessage = match
  }
  diff.keep.forEach((ad) => {
    const inAd = newVal.find(x => x.adId === ad.adId)
    inAd.message = ad.message
    inAd.hasAiMessage = ad.hasAiMessage
  })
}
