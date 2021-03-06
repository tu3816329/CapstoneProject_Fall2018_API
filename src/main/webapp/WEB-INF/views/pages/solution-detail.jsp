<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="a" %>

<style>
    #add-exercise {
		height: auto;
	}

    #solution-content {
        margin-bottom: 15px;
        padding-left: 10px;
        display: grid;
        grid-template-columns: 95% 5%;
        border: 1px solid black;
    }

    #solution-content>span:nth-child(2) {
        text-align: center;
        margin-top: 5px;
    }

	/* Exercises */
    #exercises {
        background-color: #fff;
    }

    .exercise-item {
    	min-height: 100px;
        display: grid;
        grid-template-columns: 12% 88%;
        cursor: pointer;
        margin-bottom: 20px;
    }

    .seq-no {
        background: #757575;
        color: white;
        font-style: italic;
    }

    .seq-no h1 {
        height: 100%;
        position: relative;
		margin: 0;
    }

    .seq-no span {
        top: 50%;
        left: 50%;
        position: absolute;
        transform: translateX(-50%) translateY(-50%);
        font-weight: bold;
    }

    .exercise-content {
        background: #f2f2f2;
        padding-left: 20px;
        display: grid;
        grid-template-columns: 90% 5% 5%;
    }

    .exercise-content>span:nth-child(1) {
        padding-top: 15px;
    }

	.exercise-content>span:nth-child(1)>p {
		margin-top: 0;
	}

    .exercise-content>span:nth-child(2), .exercise-content>span:nth-child(3) {
        padding-top: 10px;
		text-align: right;
    }

    .exercise-content>span:nth-child(3) {
        text-align: center;
    }

	.exercise-content>span:nth-child(4)>p {
		margin: 0 0 10px 0;
	}

    .exercise-content a {
        margin-right: 15px;
    }

    .exercise-content .fas {
        font-size: 17px;
        color: #555;
    }

    .fa-times {
        font-size: 20px !important;
    }
</style>

<%@ include file="../modals/delete-modal.jsp" %>
<div class="content-header">
    <h3 class="content-title">${solution.title}</h3>
    <div style="text-align: right">
        <a id="add-exercise" href="add-exercise?solutionId=${solution.id}" class="btn content-button">
            <i class="fas fa-plus-circle"></i> New exercise</a>
    </div>
</div>
<div id="solution-content">
    <span>${solution.content}</span>
    <span>
        <a href="edit-solution?solutionId=${solution.id}">
            <p><i style="color: #0084ff" class="fas fa-edit"></i></p>
        </a>
    </span>
</div>
<input type="hidden" value="${solution.id}" class="m-id">
<input type="hidden" value="${solution.title}" class="m-title">
<div id="exercises">
    <a:forEach items="${exercises}" var="ex" varStatus="counter">
        <div class="exercise-item">
            <div class="seq-no">
                <h1><span>${counter.count}</span></h1>
            </div>
            <div class="exercise-content">
                <span style="font-weight: bold">${ex.topic}</span>
                <span>
                    <a href="edit-exercise?exId=${ex.id}"><i class="fas fa-edit"></i></a>
                </span>
                <span>
                    <a href="delete-exercise?exId=${ex.id}&solutionId=${ex.solutionId.id}"><i class="fas fa-times"></i></a>
                </span>
                <span><hr style="border-color: black; width: 50%">${ex.answer}</span>
            </div>
        </div>
    </a:forEach>
</div>

<script>
    $(window).on('load', function () {
        $('#loading-img').fadeOut();
        $('#content').children().not('style, script').css('display', 'block');
        $('.content-header').css('display', 'grid');
        $('#solution-content').css('display', 'grid');
    });

    $(document).ready(function() {
        $('#content').css({
			'padding': 0,
			'background-color': '#e9e9e9',
			'box-shadow': 'none',
			'overflow-y': 'hidden'
		});

        $(document).on('click', '.exercise-content>span:nth-child(3)>a', function(e) {
			e.preventDefault();
			var deleteLink = $(this).attr('href');
            $('.modal').css('height', '100%');
            $('.message-modal-title').text('Delete exercise');
			$('.message-modal-body').text('Are you sure you want to delete this exercise?');
            $('.modal button:eq(0)').click(function () {
                $(this).addClass('disabled running');
                window.location.href = deleteLink;
            });
            $('.modal button:eq(1)').click(function () {
                $('.modal').css('height', '0');
            });
		});
    });
</script>