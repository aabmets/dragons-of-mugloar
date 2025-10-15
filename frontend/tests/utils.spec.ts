import { describe, it, expect, vi, beforeEach, afterEach } from "vitest";
import type * as t from "@/types";
import * as utils from "@/utils";

const EASY_PROBS = ["Piece of cake", "Walk in the park", "Sure thing"];

function makeAd(overrides: Partial<t.Advertisement> = {}): t.Advertisement {
  return {
    adId: Math.random().toString(36).slice(2),
    message: "Do a thing",
    probability: "Gamble",
    reward: 50,
    ...overrides,
  };
}

describe("formatThousands", () => {
  it("formats integers with spaces", () => {
    expect(utils.formatThousands(0)).toBe("0");
    expect(utils.formatThousands(999)).toBe("999");
    expect(utils.formatThousands(1000)).toBe("1 000");
    expect(utils.formatThousands(1234567)).toBe("1 234 567");
  });

  it("keeps decimals and sign", () => {
    expect(utils.formatThousands(1234.56)).toBe("1 234.56");
    expect(utils.formatThousands(-98765.4321)).toBe("-98 765.4321");
    expect(utils.formatThousands(1000.0)).toBe("1 000");
  });
});

describe("sleep", () => {
  beforeEach(() => vi.useFakeTimers());
  afterEach(() => vi.useRealTimers());

  it("resolves after the specified ms", async () => {
    let settled = false;
    const p = utils.sleep(100).then(() => { settled = true; });
    vi.advanceTimersByTime(99);

    await Promise.resolve();
    expect(settled).toBe(false);

    vi.advanceTimersByTime(1);
    await p;
    expect(settled).toBe(true);
  });

  it("resolves immediately for 0ms (with fake timers)", async () => {
    let done = false;
    const p = utils.sleep(0).then(() => { done = true; });
    vi.advanceTimersByTime(0);
    await p;
    expect(done).toBe(true);
  });
});

describe("isStealing", () => {
  it("detects 'steal' case-insensitively", () => {
    expect(utils.isStealing(
      makeAd({ message: "Steal the crown" }) as any)
    ).toBe(true);

    expect(utils.isStealing(
      makeAd({ message: "please STEAL it" }) as any)
    ).toBe(true);

    expect(utils.isStealing(
      makeAd({ message: "we're stealing apples" }) as any)
    ).toBe(true);
  });

  it("ignores unrelated text and empty messages", () => {
    expect(utils.isStealing(
      makeAd({ message: "Steamy buns" }) as any)
    ).toBe(false);

    expect(utils.isStealing(
      makeAd({ message: "Steel sword sale" }) as any)
    ).toBe(false);

    expect(utils.isStealing(
      makeAd({ message: undefined }) as any)
    ).toBe(false);
  });
});

describe("isTrapAdvert", () => {
  it("returns cached result when trapDetected is set", () => {
    const adTrue = makeAd({ message: "harvest carrots", trapDetected: true });
    expect(utils.isTrapAdvert(adTrue as any)).toBe(true);

    const adFalse = makeAd({
      message: "Steal bread",
      probability: EASY_PROBS[0],
      reward: 999,
      trapDetected: false,
    });
    expect(utils.isTrapAdvert(adFalse as any)).toBe(false);
  });

  it("detects trap only when stealing + easy prob (top 3) + reward > 120", () => {
    const okNotSteal = makeAd({ message: "Escort the baker", probability: EASY_PROBS[1], reward: 1000 });
    expect(utils.isTrapAdvert(okNotSteal as any)).toBe(false);

    const notEasy = makeAd({ message: "Steal a goat", probability: "Gamble", reward: 999 });
    expect(utils.isTrapAdvert(notEasy as any)).toBe(false);

    const notRich = makeAd({ message: "Steal a goat", probability: EASY_PROBS[2], reward: 120 });
    expect(utils.isTrapAdvert(notRich as any)).toBe(false);

    const trap = makeAd({ message: "Steal a goat", probability: EASY_PROBS[2], reward: 121 });
    expect(utils.isTrapAdvert(trap as any)).toBe(true);

    expect(trap.trapDetected).toBe(true);
  });
});

describe("diffMessageBoards", () => {
  it("returns add/remove/keep with identity preserved for keep (from old)", () => {
    const a1 = makeAd({ adId: "1", message: "old-1" });
    const a2_old = makeAd({ adId: "2", message: "old-2" });
    const oldBoard: t.MessageBoard = [a1, a2_old];

    const a2_new = { ...a2_old, message: "new-2" };
    const a3 = makeAd({ adId: "3", message: "new-3" });
    const newBoard: t.MessageBoard = [a2_new, a3];

    const diff = utils.diffMessageBoards(oldBoard as any, newBoard as any) as any;
    expect(diff.add.map((x: t.Advertisement) => x.adId)).toEqual(["3"]);
    expect(diff.remove.map((x: t.Advertisement) => x.adId)).toEqual(["1"]);
    expect(diff.keep.map((x: t.Advertisement) => x.adId)).toEqual(["2"]);

    expect(diff.keep[0]).toBe(a2_old);
    expect(diff.keep[0].message).toBe("old-2");
  });
});

describe("maybeReplaceMessage", () => {
  beforeEach(() => {
    vi.spyOn(console, "log").mockImplementation(() => {});
  });
  afterEach(() => {
    (console.log as any).mockRestore?.();
  });

  it("replaces exactly one newly-added ad when not trap/steal and AI messages available", () => {
    const existing = makeAd({ adId: "1", message: "prev", hasAiMessage: false });
    const oldBoard: t.MessageBoard = [existing];
    const newAd = makeAd({ adId: "2", message: "original text", probability: "Gamble", reward: 10 });
    const newBoard: t.MessageBoard = [existing, newAd];

    const ai = ["AI #1", "AI #2"];
    utils.maybeReplaceMessage(oldBoard as any, newBoard as any, ai);

    expect(newAd.message).toBe("AI #2");
    expect(newAd.hasAiMessage).toBe(true);
    expect(ai).toEqual(["AI #1"]);
  });

  it("does not replace when the newly-added ad is a trap", () => {
    const oldBoard: t.MessageBoard = [];
    const trap = makeAd({
      adId: "t1",
      message: "Steal from the king",
      probability: EASY_PROBS[0],
      reward: 500,
    });
    const newBoard: t.MessageBoard = [trap];

    const ai = ["AI msg"];
    utils.maybeReplaceMessage(oldBoard as any, newBoard as any, ai);

    expect(trap.message).toBe("Steal from the king");
    expect(trap.hasAiMessage).toBe(false);
    expect(ai).toEqual(["AI msg"]);
  });

  it("propagates prior AI message and flag for kept ads", () => {
    const keptOld = makeAd({ adId: "k1", message: "Old AI text", hasAiMessage: true });
    const oldBoard: t.MessageBoard = [keptOld];

    const keptNew = makeAd({ adId: "k1", message: "Server's latest text", hasAiMessage: false });
    const alsoNew = makeAd({ adId: "n1", message: "Brand new", probability: "Gamble", reward: 10 });
    const newBoard: t.MessageBoard = [keptNew, alsoNew];

    const ai: string[] = [];
    utils.maybeReplaceMessage(oldBoard as any, newBoard as any, ai);

    expect(keptNew.message).toBe("Old AI text");
    expect(keptNew.hasAiMessage).toBe(true);

    expect(alsoNew.message).toBe("Brand new");
    expect(alsoNew.hasAiMessage).toBe(false);
  });

  it("does nothing special when there are 0 or >1 added ads besides syncing keep", () => {
    const a1_old = makeAd({ adId: "a1", message: "AI prev", hasAiMessage: true });
    const a1_new = makeAd({ adId: "a1", message: "server text" });
    const oldBoard0: t.MessageBoard = [a1_old];
    const newBoard0: t.MessageBoard = [a1_new];

    utils.maybeReplaceMessage(oldBoard0 as any, newBoard0 as any, ["X"]);
    expect(a1_new.message).toBe("AI prev");
    expect(a1_new.hasAiMessage).toBe(true);

    const oldBoard2: t.MessageBoard = [];
    const n1 = makeAd({ adId: "n1", message: "one" });
    const n2 = makeAd({ adId: "n2", message: "two" });
    const newBoard2: t.MessageBoard = [n1, n2];
    const ai = ["A", "B"];

    utils.maybeReplaceMessage(oldBoard2 as any, newBoard2 as any, ai);
    expect(n1.message).toBe("one");
    expect(n2.message).toBe("two");
    expect(ai).toEqual(["A", "B"]);
  });
});
