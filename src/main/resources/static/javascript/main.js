/**
 * @description:
 * nickName: Lei钟意
 * email: lxfleixiaofeng@163.com
 * wechat: ConsoleWarn
 * 尊重原创，请保留版权！
 * @author Lei Xiaofeng
 * @date 2023/05/10
 */
const sound = new Howl({src: ["/static/asset/dong.mp3"]});
const bgm = new Howl({
    src: ["/static/asset/bgm.mp3"],
    html5: true,
    loop: true,
    volume: 0.2,
});

function hideLoading() {
    const loadingElement = document.querySelector("#loading");
    if (loadingElement) {
        loadingElement.remove();
    }
}

async function duang() {
    if (id === -1) {
        return;
    }
    const url = `/api/user/${id}/duang`;

    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
            // 可以根据需要添加其他配置，如身份验证令牌
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}


window.onload = () => {
    hideLoading();

    let ringId = 0;
    let bgmId = 0;
    let count = preCount + preAutoCount;
    let countFlag = false;
    let autoClick = false;
    let autoClickInterval = null;
    let countElement = document.querySelector(".count");
    let woodenFishElement = document.querySelector(".woodenFish");
    let logoElement = document.querySelector(".logo");
    let centerElement = document.querySelector("#center");


    function startAnimate() {
        countElement.style.transform = "scale(1.1)";
        woodenFishElement.style.transform = "scale(.95)";
        const div = document.createElement("div");
        div.classList.add("subtitleCountTip");
        div.innerText = `@${to} 功德+1`;
        centerElement.appendChild(div);
        setTimeout(() => {
            div.remove();
        }, 1000);
    }

    function initAnimate() {
        countElement.style.transform = "scale(1)";
        woodenFishElement.style.transform = "scale(1)";
    }

    function counter() {
        countFlag = true;
        count++;
        countElement.innerHTML = String(count);
        startAnimate();
        if (ringId !== 0) {
            if (sound.playing()) {
                sound.stop(ringId);
            }
            sound.play(ringId);
        } else {
            ringId = sound.play();
        }
    }

    counter();

    document.onkeyup = (e) => {
        if (e.key === " ") {
            if (!countFlag) {
                counter();
            }
        }
    };

    document.onkeydown = (e) => {
        if (e.key === " ") {
            countFlag = false;
            initAnimate();
        }
    };

    woodenFishElement.addEventListener("mouseup", async () => {
        counter();
        await duang();
    });

    woodenFishElement.addEventListener("mousedown", () => {
        setTimeout(() => {
            initAnimate();
        }, 200);
    });

    logoElement.addEventListener("click", () => {
        if (bgm.playing() && bgm.state().toString() === "loaded") {
            bgm.pause(bgmId);
        } else {
            if (bgmId !== 0) {
                bgm.play(bgmId);
            } else {
                bgmId = bgm.play();
            }
        }
    });


    function remChange(doc, win) {
        let docEl = doc.documentElement;
        let resizeEvt =
            "orientationchange" in window ? "orientationchange" : "resize";
        let recalc = function () {
            var clientWidth = docEl.clientWidth;
            if (!clientWidth) return;
            if (clientWidth >= 640) {
                docEl.style.fontSize = "100px";
            } else {
                docEl.style.fontSize = 100 * (clientWidth / 640) + "px";
            }
        };
        if (!doc.addEventListener) return;
        win.addEventListener(resizeEvt, recalc, false);
        doc.addEventListener("DOMContentLoaded", recalc, false);
        recalc();
    }

    remChange(document, window);

    !(function (p) {
        "use strict";
        !(function (t) {
            var s = window,
                e = document,
                i = p,
                c = "".concat(
                    "/static/javascript/js-sdk-pro.min.js"
                ),
                n = e.createElement("script"),
                r = e.getElementsByTagName("script")[0];
            (n.type = "text/javascript"),
                n.setAttribute("charset", "UTF-8"),
                (n.async = !0),
                (n.src = c),
                (n.id = "LA_COLLECT"),
                (i.d = n);
            var o = function () {
                s.LA.ids.push(i);
            };
            s.LA ? s.LA.ids && o() : ((s.LA = p), (s.LA.ids = []), o()),
                r.parentNode.insertBefore(n, r);
        })();
    })({id: "K3jk2A0VEE1CPg1Y", ck: "K3jk2A0VEE1CPg1Y"});

    autoClickInterval = setInterval(() => {
        counter();
        setTimeout(() => {
            initAnimate();
        }, 200);
    }, 1000);
}
