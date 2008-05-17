
RM := rm -rf

# All of the sources participating in the build are defined here
C_SRCS +=

CPP_SRCS += \
./idl_acr.cpp \
./idlhelper.cpp 

OBJS += \
./idl_acr.o \
./idlhelper.o 

C_DEPS += 

CPP_DEPS += \
./idl_acr.d \
./idlhelper.d 


ifneq ($(MAKECMDGOALS),clean)
ifneq ($(strip $(C++_DEPS)),)
-include $(C++_DEPS)
endif
ifneq ($(strip $(CC_DEPS)),)
-include $(CC_DEPS)
endif
ifneq ($(strip $(C_DEPS)),)
-include $(C_DEPS)
endif
ifneq ($(strip $(CPP_DEPS)),)
-include $(CPP_DEPS)
endif
ifneq ($(strip $(CXX_DEPS)),)
-include $(CXX_DEPS)
endif
ifneq ($(strip $(C_UPPER_DEPS)),)
-include $(C_UPPER_DEPS)
endif
endif
# Each subdirectory must supply rules for building sources it contributes
%.o: %.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -I"../core" -I"../xmlrpc" -I"../" -I/Applications/itt/idl64/external/include -O0 -g3 -Wall -c -fmessage-length=0 -dynamic -v -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o"$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '

%.o: %.c
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C Compiler'
	gcc -I"../core" -I"../xmlrpc" -I"../"  -I/Applications/itt/idl64/external/include -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o"$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


# Add inputs and outputs from these tool invocations to the build variables 

# All Target
all: acr_idl.so

# Tool invocations
acr_idl.so: $(OBJS) $(USER_OBJS)
	@echo 'Building target: $@'
	@echo 'Invoking: MacOS X C++ Linker'
	g++ -L/Applications/itt/idl64/bin/bin.darwin.i386 -undefined dynamic_lookup -dynamiclib -o "acr_idl.so" ../libacr-capi.so $(OBJS) $(USER_OBJS) $(LIBS)
	@echo 'Finished building target: $@'
	@echo ' '


# Other Targets
clean:
	-$(RM) $(OBJS)$(C++_DEPS)$(CC_DEPS)$(C_DEPS)$(CPP_DEPS)$(LIBRARIES)$(CXX_DEPS)$(C_UPPER_DEPS) acr_idl.so
	-@echo ' '

.PHONY: all clean dependents
.SECONDARY:

