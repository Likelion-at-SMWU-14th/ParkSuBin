package com.likelion.seminar;

import com.likelion.seminar.entity.Author;
import com.likelion.seminar.entity.Comment;
import com.likelion.seminar.entity.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BoardMappingTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void mappingAndCascadePersistTest() {
        // 1. 작성자는 별도로 저장
        Author postAuthor = new Author("수빈");
        Author commentAuthor = new Author("아기사자");

        entityManager.persist(postAuthor);
        entityManager.persist(commentAuthor);

        // 2. 같은 작성자가 게시글 두 개 작성
        Post firstPost = new Post(
                "6주차 실습",
                "게시글과 댓글 매핑 실습입니다.",
                postAuthor
        );

        Post secondPost = new Post(
                "두 번째 글",
                "한 작성자가 여러 게시글을 작성합니다.",
                postAuthor
        );

        // 3. 첫 게시글에 댓글 두 개 추가
        Comment firstComment =
                firstPost.addComment("잘 읽었습니다!", commentAuthor);

        firstPost.addComment("감사합니다!", postAuthor);

        // 4. 게시글만 저장: 댓글은 CascadeType.PERSIST로 함께 저장
        entityManager.persist(firstPost);
        entityManager.persist(secondPost);

        entityManager.flush();

        Long firstPostId = firstPost.getId();
        Long secondPostId = secondPost.getId();
        Long firstCommentId = firstComment.getId();
        Long postAuthorId = postAuthor.getId();
        Long commentAuthorId = commentAuthor.getId();

        assertThat(firstCommentId).isNotNull();

        // 메모리에 있던 객체가 아니라 DB에서 다시 조회
        entityManager.clear();

        // 5. 게시글 → 작성자 관계 검증
        Post foundPost = entityManager.find(Post.class, firstPostId);
        Post foundSecondPost =
                entityManager.find(Post.class, secondPostId);

        assertThat(foundPost.getAuthor().getId())
                .isEqualTo(postAuthorId);

        assertThat(foundSecondPost.getAuthor().getId())
                .isEqualTo(postAuthorId);

        // 6. 게시글 → 댓글 목록 검증
        assertThat(foundPost.getComments()).hasSize(2);

        assertThat(foundPost.getComments())
                .extracting(Comment::getContent)
                .containsExactlyInAnyOrder(
                        "잘 읽었습니다!",
                        "감사합니다!"
                );

        // 7. 댓글 → 게시글, 작성자 관계 검증
        Comment foundComment =
                entityManager.find(Comment.class, firstCommentId);

        assertThat(foundComment).isNotNull();

        assertThat(foundComment.getPost().getId())
                .isEqualTo(firstPostId);

        assertThat(foundComment.getAuthor().getId())
                .isEqualTo(commentAuthorId);
    }
}