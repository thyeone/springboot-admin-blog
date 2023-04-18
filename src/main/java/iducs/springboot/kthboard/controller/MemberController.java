package iducs.springboot.kthboard.controller;

import iducs.springboot.kthboard.domain.Member;
import iducs.springboot.kthboard.domain.PageRequestDTO;
import iducs.springboot.kthboard.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/members") // localhost:8888/members로 시작
public class MemberController {

    public final MemberService memberService;
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // registration form 멤버 등록 페이지
    @GetMapping("/regform")
    public String getRegform(Model model) {
        model.addAttribute("member", Member.builder().build());
        return "members/regform";  // view resolving
    }

    @GetMapping("")
    public String getMembers(PageRequestDTO pageRequestDTO, Model model) {
        model.addAttribute("list", memberService.readListBy(pageRequestDTO));
        return "members/members";    // view resolving
    }

    // 멤버 등록
    @PostMapping("")
    public String postMember(@ModelAttribute("member") Member member, Model model) {
        memberService.create(member);
        model.addAttribute("member", member);
        return "members/login";
    }

    // 상세 페이지
    @GetMapping("/{idx}")
    public String getMember(@PathVariable("idx") Long seq, Model model) {
        Member member = memberService.readById(seq);
        model.addAttribute("member", member);
        return "members/member";   // view resolving : member.html
    }

    // 수정 페이지
    @GetMapping("/{idx}/upform")
    public String getUpform(@PathVariable("idx") Long seq, Model model) {
        // 정보를 전달받을 빈(empty) 객체를 보냄
        Member member = memberService.readById(seq);
        model.addAttribute("member", member);
        return "members/upform";
    }

    // 멤버 정보 수정
    @PutMapping("/{idx}")
    public String putMember(@ModelAttribute("member") Member member, Model model) { // ModelAttribute는 form에서 가져오는 거
        memberService.update(member);
        model.addAttribute("member", member);
        return "members/member";
    }

    // 삭제 페이지
    @GetMapping("/{idx}/delform")
    public String getDelForm(@PathVariable("idx") Long seq, Model model) {
        // 정보를 전달받을 빈(empty) 객체를 보냄
        Member member = memberService.readById(seq);
        model.addAttribute("member", member);
        return "members/delform";
    }

    // 멤버 삭제
    @DeleteMapping("/{idx}")
    public String deleteMember(@ModelAttribute("idx") Long seq) {
        Member member = memberService.readById(seq);
        memberService.delete(member);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLoginForm(Model model) {
        model.addAttribute("member", Member.builder().build());
        return "members/login";
    }
    @PostMapping("/login")
    public String postLogin(@ModelAttribute("member") Member member, HttpServletRequest request) {
        Member dto = null;
        if((dto = memberService.loginByEmail(member)) != null) {
            HttpSession session = request.getSession();
            session.setAttribute("login", dto);
            if(dto.getId().contains("admin")) {
                session.setAttribute("isadmin", dto.getId());
                System.out.println(session.getAttribute("isadmin"));
            }
            return "redirect:/";
        }
        else return "members/login";
    }
    @GetMapping("/logout")
    public String getLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

}
