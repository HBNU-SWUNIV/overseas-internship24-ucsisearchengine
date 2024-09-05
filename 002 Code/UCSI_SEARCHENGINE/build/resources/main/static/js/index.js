document.addEventListener('DOMContentLoaded', function() {
    const domain = "http://localhost:8080";
    const button = document.querySelector('.type-button');
    const icon = document.querySelector('.type-icon');
    const list = document.querySelector('.list');
    const searchForm = document.querySelector('form');
    const historyBox = document.querySelector('.history');

    if (button != null) { // 한줄이라도 오류 발생시 다음 밑 코드가 실행이 안됨.
        button.addEventListener('click', function() {
            list.classList.toggle('open');
            icon.classList.toggle('rotated');
        });
    }

    document.querySelector('.search-button').addEventListener('click', function(event) {
        const searchInput = document.querySelector('.search-input');
        const errorCode = document.querySelector('.error-code');

        if (searchInput.value.trim() === '') {
            event.preventDefault(); // Prevent form submission
            errorCode.classList.add('visible'); // Show error message

            // Hide the error message after 2 seconds
            setTimeout(function() {
                errorCode.classList.remove('visible');
            }, 1500);
        }
    });

    const currentUrl = window.location.href;

    if (currentUrl.startsWith(domain + "/result")) {
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const keyword = urlParams.get('keyword');

        if (keyword) {
            localStorage.setItem('searchKeyword', keyword);
        }
    } else {
        console.log('URL does not match the specified pattern.');
    }

    document.querySelectorAll('.list li').forEach(function(li) {
        li.addEventListener('click', function() {
            const type = li.getAttribute('data-type').trim();
            const keyword = localStorage.getItem('searchKeyword');
            console.log("click");
            $('.loader-container').show();

            const data = {
                searchKeyword: keyword,
                type: type
            };

            $.ajax({
                url: '/result',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(data),
                dataType: 'html',
                success: function(fragment) {
                    $('#result-wrapper').replaceWith(fragment);
                },
                error: function(xhr, status, error) {
                    console.error('Error:', error);
                },
                complete: function () {
                    $('.loader-container').hide();
                }
            });
        });
    });

    const searchInput = document.querySelector('.search-input');
    const searchContainer = document.querySelector('.search-container');
    const resultSearch = document.querySelector('.search-area > form > .search-container > .search-input');

    let searchHistory = JSON.parse(localStorage.getItem('searchHistory'));

    if (!searchHistory) {
        searchHistory = [];
        localStorage.setItem('searchHistory', JSON.stringify(searchHistory));
    }

    searchHistory = JSON.parse(localStorage.getItem('searchHistory'));
    function displaySearchHistory() {
        const historyContent = document.querySelector('.history-content');

        // 기존 리스트가 있다면 제거
        historyContent.innerHTML = '';

        // 검색 기록을 리스트로 만들어서 .history-content에 추가
        searchHistory.forEach(function(item) {
            const gridItem = document.createElement('div');
            gridItem.classList.add('history-item');
            gridItem.textContent = item;
            gridItem.addEventListener('click', function() {
                searchInput.value = item; // 클릭한 검색어로 인풋 채우기
                searchInput.focus(); // 포커스를 유지
            });
            historyContent.appendChild(gridItem);
        });
    }

    searchForm.addEventListener('submit', function () {
        const keyword = searchInput.value.trim();
        if (keyword && !searchHistory.includes(keyword)) {
            searchHistory.push(keyword);

            if (searchHistory.length > 5) {
                searchHistory.shift();
            }

            localStorage.setItem('searchHistory', JSON.stringify(searchHistory));
        }
    });

    if (searchHistory.length > 0) {
        searchInput.addEventListener('click', function () {
            historyBox.classList.add('visible');
            searchInput.style.boxShadow = 'none';
            searchInput.classList.add('click');
            displaySearchHistory();
        });

        searchInput.addEventListener('blur', function () {
            // 잠시 후에 숨김 (애니메이션이 끝난 후에 숨김)
            setTimeout(() => {
                historyBox.classList.remove('visible');
                searchInput.style.boxShadow = '3px 5px 10px rgba(10, 10, 10, 0.3)';
                resultSearch.style.boxShadow = 'none';
            }, 300); // 애니메이션 시간과 일치시킴
            searchInput.classList.remove('click');
        });
    }

    const inputField = document.querySelector('.search-area > form > div > input');

    inputField.addEventListener('click', function() {
        // 클릭 시 .clicked 클래스를 추가하여 배경색 변경
        inputField.classList.add('clicked');
    });

    // 클릭 외 다른 곳을 클릭했을 때 배경색을 원래대로 되돌리기
    document.addEventListener('click', function(event) {
        if (!inputField.contains(event.target)) {
            // 입력 필드 외의 곳이 클릭된 경우 배경색을 원래대로 복원
            setTimeout(() => {
                inputField.classList.remove('clicked');
            }, 300); // 애니메이션 시간과 일치시킴
        }
    });

    function displayPathWithImages(path) {
        const pathContainer = document.querySelector('.path-container');
        const parts = path.split('/'); // 경로를 '/'로 나눔

        pathContainer.innerHTML = ''; // 기존 내용을 제거

        parts.forEach((part, index) => {
            if (part) {
                const partElement = document.createElement('div');
                partElement.classList.add('path-part');

                if (index < parts.length) { // 마지막 부분이 아닌 경우
                    const img = document.createElement('img');
                    img.src = '/icon/folder.png'; // 이미지 경로를 설정
                    partElement.appendChild(img);
                }

                const textNode = document.createTextNode(part);
                partElement.appendChild(textNode);

                pathContainer.appendChild(partElement);
            }
        });
    }

    let timeoutId;

    document.addEventListener('click', function(event) {
        const resultContent = event.target.closest('.result-content');

        if (resultContent) {
            const absolutePathElement = resultContent.querySelector('.absolutePath');
            const textToCopy = absolutePathElement ? absolutePathElement.textContent : '';

            navigator.clipboard.writeText(textToCopy).then(function() {
                console.log(textToCopy);
                const successClipboard = document.querySelector('.success-clipboard');

                displayPathWithImages(textToCopy);

                event.preventDefault();

                if (timeoutId) {
                    clearTimeout(timeoutId);
                }

                successClipboard.classList.add('visible');

                timeoutId = setTimeout(function() {
                    successClipboard.classList.remove('visible');
                }, 2000);
            }).catch(function(err) {
                console.error('Fail Save Clipboard : ', err);
            });
        }
    });



});
