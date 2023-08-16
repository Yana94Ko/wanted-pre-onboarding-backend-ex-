package wanted.preonboarding.backend.member.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.preonboarding.backend.member.dto.MemberRequest;
import wanted.preonboarding.backend.member.service.MemberService;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/signup")
    ResponseEntity<String> signUpMember(@Valid @RequestBody MemberRequest.Create memberDto){
        memberDto.setPassword(bCryptPasswordEncoder.encode(memberDto.getPassword()));
        memberService.signUpMember(memberDto);
        return new ResponseEntity<>(memberDto.getEmail() + "님, 회원가입에 성공했습니다 \n환영합니다.", HttpStatus.OK);
    }

}
